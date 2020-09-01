package cn.limbo.auth.service;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.IdUtil;
import cn.limbo.auth.enums.ModelStatus;
import cn.limbo.auth.model.CasbinModel;
import cn.limbo.auth.entity.SysUserEnforceEntity;
import cn.limbo.auth.repo.CasbinModelRepo;
import lombok.extern.slf4j.Slf4j;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EnforcerServiceImpl implements EnforcerService {

    private final CasbinModelRepo casbinModelRepo;
    private final Enforcer enforcer;

    public EnforcerServiceImpl(CasbinModelRepo casbinModelRepo, Enforcer enforcer) {
        this.casbinModelRepo = casbinModelRepo;
        this.enforcer = enforcer;
    }

    @Override
    public List<CasbinModel> showModels() {
        return casbinModelRepo.findAll();
    }

    @Override
    public CasbinModel saveModel(String modelText) {
        CasbinModel casbinModel = new CasbinModel();
        casbinModel.setId(IdUtil.fastSimpleUUID());
        casbinModel.setModelDef(modelText);
        casbinModel.setModelStatus(ModelStatus.INFORCE.name());
        return casbinModelRepo.save(casbinModel);
    }

    @Override
    @CacheEvict(cacheNames = {"CASBIN_MODEL_TEXT"}, key = "#modelId", cacheManager = "casbinRedisCacheManager")
    public Integer updateModelDef(String modelId, String modelText) {
        return casbinModelRepo.setModelDefFor(modelText, modelId);
    }

    @Override
    public Integer updateModelStatus(String modelId, String modelStatus) {
        if (EnumUtil.notContains(ModelStatus.class, modelStatus)) {
            log.warn("Un existed model status {}.", modelStatus);
            return null;
        }
        return casbinModelRepo.setModelStatusFor(modelStatus, modelId);
    }

    @Override
    public List<List<String>> showPolicies() {
        return enforcer.getPolicy();
    }

    @Override
    public String removePolicy(String policy) {
        String[] strings = policy.split(",");
        try {
            enforcer.removePolicy(strings);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed removing this policy.";
        }
        return "success";
    }

    @Override
    public String savePolicy(String policy) {
        String[] strings = policy.split(",");
        enforcer.addPolicy(strings);
        try {
            enforcer.savePolicy();
        } catch (Exception e) {
            e.printStackTrace();
            return "The policy has been filtered and can not be persisted.";
        }
        return "success";
    }

    @Override
    public Boolean enforceSysUser(SysUserEnforceEntity sysUserEnforceEntity) {
        String text = fetchModelText(sysUserEnforceEntity.getModelId());
        Model model = buildModelFromText(text);
        enforcer.setModel(model);
        enforcer.loadPolicy();
        boolean f = enforcer.enforce(sysUserEnforceEntity.getUnit(), sysUserEnforceEntity.getUser(), sysUserEnforceEntity.getObj(), sysUserEnforceEntity.getAct());
        if (f) {
            log.info("User {} 's action {} on object {} has been permitted.", sysUserEnforceEntity.getUser(), sysUserEnforceEntity.getAct(), sysUserEnforceEntity.getObj());
        } else {
            log.warn("User {} 's action {} on object {} has been denied.", sysUserEnforceEntity.getUser(), sysUserEnforceEntity.getAct(), sysUserEnforceEntity.getObj());
        }
        return f;
    }

    private Model buildModelFromText(String text) {
        Model model = new Model();
        model.loadModelFromText(text);
        return model;
    }

    @Cacheable(cacheNames = {"CASBIN_MODEL_TEXT"}, key = "#modelId", sync = true, cacheManager = "casbinRedisCacheManager")
    public String fetchModelText(String modelId) {
        return casbinModelRepo.findCasbinModelById(modelId).getModelDef();
    }


}

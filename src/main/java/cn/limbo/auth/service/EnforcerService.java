package cn.limbo.auth.service;

import cn.limbo.auth.model.CasbinModel;
import cn.limbo.auth.entity.SysUserEnforceEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EnforcerService {

    List<CasbinModel> showModels();

    CasbinModel saveModel(String modelText);

    Integer updateModelDef(String modelId, String modelText);

    Integer updateModelStatus(String modelId, String modelStatus);

    List<List<String>> showPolicies();

    String removePolicy(String policy);

    String savePolicy(String policy);

    Boolean enforceSysUser(SysUserEnforceEntity sysUserEnforceEntity);
}

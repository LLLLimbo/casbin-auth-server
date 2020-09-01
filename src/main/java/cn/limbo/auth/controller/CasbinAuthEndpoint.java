package cn.limbo.auth.controller;

import cn.limbo.auth.entity.AlterCasbinModel;
import cn.limbo.auth.entity.AlterCasbinPolicy;
import cn.limbo.auth.model.CasbinModel;
import cn.limbo.auth.entity.SysUserEnforceEntity;
import cn.limbo.auth.service.EnforcerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class CasbinAuthEndpoint {

    private final EnforcerService enforcerService;

    public CasbinAuthEndpoint(EnforcerService enforcerService) {
        this.enforcerService = enforcerService;
    }

    @GetMapping("/showModels")
    public ResponseEntity<List<CasbinModel>> showModels() {
        return ResponseEntity.of(Optional.of(enforcerService.showModels()));
    }

    @GetMapping("/showPolicies")
    public ResponseEntity<List<List<String>>> showPolicies() {
        return ResponseEntity.of(Optional.of(enforcerService.showPolicies()));
    }

    @PostMapping("/saveModel")
    public ResponseEntity<CasbinModel> saveModel(@RequestBody AlterCasbinModel alterCasbinModel) {
        return ResponseEntity.of(Optional.of(enforcerService.saveModel(alterCasbinModel.getModelDef())));
    }

    @PostMapping("/updateModelDef")
    public ResponseEntity<Integer> updateModelDef(@RequestBody AlterCasbinModel alterCasbinModel) {
        return ResponseEntity.of(
                Optional.of(enforcerService.updateModelDef(alterCasbinModel.getModelId(), alterCasbinModel.getModelDef()))
        );
    }

    @PostMapping("/updateModelStatus")
    public ResponseEntity<Integer> updateModelStatus(@RequestBody AlterCasbinModel alterCasbinModel) {
        return ResponseEntity.of(
                Optional.of(enforcerService.updateModelStatus(alterCasbinModel.getModelId(), alterCasbinModel.getModelStatus()))
        );
    }

    @PostMapping("/savePolicy")
    public ResponseEntity<String> savePolicy(@RequestBody AlterCasbinPolicy alterCasbinPolicy) {
        return ResponseEntity.of(Optional.of(enforcerService.savePolicy(alterCasbinPolicy.getPolicy())));
    }

    @PostMapping("/removePolicy")
    public ResponseEntity<String> removePolicy(@RequestBody AlterCasbinPolicy alterCasbinPolicy) {
        return ResponseEntity.of(Optional.of(enforcerService.removePolicy(alterCasbinPolicy.getPolicy())));
    }

    @PostMapping("/ask")
    public ResponseEntity<Boolean> askPermission(@RequestBody SysUserEnforceEntity enforceEntity) {
        return ResponseEntity.of(Optional.of(enforcerService.enforceSysUser(enforceEntity)));
    }
}

package cn.limbo.auth.repo;

import cn.limbo.auth.model.CasbinModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CasbinModelRepo extends CrudRepository<CasbinModel, Long> {

    List<CasbinModel> findAll();

    CasbinModel findCasbinModelById(String id);

    @Modifying
    @Transactional
    @Query("update CasbinModel m set m.modelDef=?1 where m.id=?2")
    Integer setModelDefFor(String modelDef, String id);

    @Modifying
    @Transactional
    @Query("update CasbinModel m set m.modelStatus=?1 where m.id=?2")
    Integer setModelStatusFor(String modelStatus, String id);

}

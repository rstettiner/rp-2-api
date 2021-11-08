package com.rafa.repository.base;

import com.rafa.domain.base.SysMessageLkup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysMessageLkupRepository extends JpaRepository<SysMessageLkup, Long> {
    
    List<SysMessageLkup> findAllByStatusCd(String statusCd);

    List<SysMessageLkup> findBySubsystemCdAndMessageCd(String subsytemCd, String messageCd);
}

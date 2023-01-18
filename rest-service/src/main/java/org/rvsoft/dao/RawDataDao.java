package org.rvsoft.dao;

import org.rvsoft.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataDao extends JpaRepository<RawData, Long>
{
}

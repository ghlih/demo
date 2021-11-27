package cn.hwali.hr.mapper;

import cn.hwali.hr.model.MntDatabase;

public interface MntDatabaseMapper {
    int deleteByPrimaryKey(String dbId);

    int insert(MntDatabase record);

    int insertSelective(MntDatabase record);

    MntDatabase selectByPrimaryKey(String dbId);

    int updateByPrimaryKeySelective(MntDatabase record);

    int updateByPrimaryKey(MntDatabase record);
}
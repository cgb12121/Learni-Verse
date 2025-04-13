package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Unit;
import com.hanu.leaniverse.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitServiceImpl implements UnitService {
    @Autowired
    private UnitRepository unitRepository;

    public Unit getUnitById(int unitId) {
        return unitRepository.findById(unitId).get();
    }
}

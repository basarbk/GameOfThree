package com.basarbk.gameofthree.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basarbk.gameofthree.model.Action;

public interface ActionDao extends JpaRepository<Action, Long> {

}

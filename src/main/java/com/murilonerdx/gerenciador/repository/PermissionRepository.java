package com.murilonerdx.gerenciador.repository;

import com.murilonerdx.gerenciador.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}

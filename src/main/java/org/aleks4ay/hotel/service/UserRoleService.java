package org.aleks4ay.hotel.service;


import java.sql.Connection;
import java.util.*;

class UserRoleService {

    /*public Role getById(Long id) {
        Connection conn = ConnectionPool.getConnection();
        UserRoleDao roleDao = new UserRoleDao(conn);
        Role role = roleDao.getById(id);
        ConnectionPool.closeConnection(conn);
        return role;
    }

    Map<Long, Role> getAllRoleAsMap() {
        Connection conn = ConnectionPool.getConnection();
        UserRoleDao roleDao = new UserRoleDao(conn);
        Map<Long, Role> roleMap = roleDao.getAllRoleAsMap();
        ConnectionPool.closeConnection(conn);
        return roleMap;
    }

    public boolean delete(Long id, Role role) {
        Connection conn = ConnectionPool.getConnection();
        UserRoleDao roleDao = new UserRoleDao(conn);
        boolean result = roleDao.delete(id, role);
        ConnectionPool.closeConnection(conn);
        return result;
    }

    public boolean create(long id, Role role) {
        Connection conn = ConnectionPool.getConnection();
        UserRoleDao roleDao = new UserRoleDao(conn);
        boolean result =  roleDao.createRole(id, role);
        ConnectionPool.closeConnection(conn);
        return result;
    }*/
}

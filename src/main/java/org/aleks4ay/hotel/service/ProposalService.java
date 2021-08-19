package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.model.Proposal;
import org.aleks4ay.hotel.model.User;

import java.sql.Connection;
import java.util.*;

public class ProposalService {
    private UserService userService = new UserService();

   /* public Optional<Proposal> getById(Long id) {
        Connection conn = ConnectionPool.getConnection();
        ProposalDao proposalDao = new ProposalDao(conn);
        Optional<Proposal> proposalOptional = proposalDao.findById(id);
        if (proposalOptional.isPresent()) {
            Proposal proposal = proposalOptional.get();
            proposal.setUser(userService.getById(proposal.getUser().getId()).orElse(null));
        }
        ConnectionPool.closeConnection(conn);
        return proposalOptional;
    }

    public List<Proposal> getAll() {
        Connection conn = ConnectionPool.getConnection();
        ProposalDao proposalDao = new ProposalDao(conn);
        List<Proposal> proposals = proposalDao.findAll();
        Map<Long, User> users = new HashMap<>();

        for (User u : userService.getAll()) {
            users.put(u.getId(), u);
        }

        for (Proposal p : proposals) {
            p.setUser(users.get(p.getUser().getId()));
        }
        ConnectionPool.closeConnection(conn);
        return proposals;
    }

    public List<Proposal> getAllByUser(User user) {
        Connection conn = ConnectionPool.getConnection();
        ProposalDao proposalDao = new ProposalDao(conn);
        List<Proposal> orders = new ArrayList<>();

        for (Proposal o : proposalDao.findAll()) {
            if (o.getUser().getId() == user.getId()) {
                o.setUser(user);
                orders.add(o);
            }
        }
        ConnectionPool.closeConnection(conn);
        return orders;
    }


    public Optional<Proposal> save(Proposal proposal) {
        Connection conn = ConnectionPool.getConnection();
        ProposalDao proposalDao = new ProposalDao(conn);
        proposal.setStatus(Proposal.Status.NEW);
        Optional<Proposal> proposalOptional = proposalDao.save(proposal);
        ConnectionPool.closeConnection(conn);
        return proposalOptional;
    }
*//*
    public List<Proposal> getAll(int positionOnPage, int page) {
        Connection conn = ConnectionPool.getConnection();
        ProposalDao proposalDao = new ProposalDao(conn);
        List<Proposal> proposals = proposalDao.findAll(positionOnPage, page);
        ConnectionPool.closeConnection(conn);
        return proposals;
    }*//*

    public List<Proposal> doPagination(int positionOnPage, int page, List<Proposal> entities) {
        return new UtilService<Proposal>().doPagination(positionOnPage, page, entities);
    }

    public boolean updateStatus(Proposal.Status status, long id) {
        Connection conn = ConnectionPool.getConnection();
        ProposalDao proposalDao = new ProposalDao(conn);
        boolean result = proposalDao.updateStatus(status.toString(), id);
        ConnectionPool.closeConnection(conn);
        return result;
    }*/
}

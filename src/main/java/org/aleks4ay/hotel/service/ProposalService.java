package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Proposal;
import org.aleks4ay.hotel.model.User;
import org.aleks4ay.hotel.repository.ProposalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProposalService {
    @Autowired
    private ProposalRepo proposalRepo;

    public Optional<Proposal> getById(Long id) {
        Optional<Proposal> proposalOptional = proposalRepo.findById(id);
/*        if (proposalOptional.isPresent()) {
            Proposal proposal = proposalOptional.get();
            proposal.setUser(userService.getById(proposal.getUser().getId()).orElse(null));
        }*/
        return proposalOptional;
    }

    public List<Proposal> getAll() {
        List<Proposal> proposals = (List<Proposal>) proposalRepo.findAll();
/*
        Map<Long, User> users = new HashMap<>();

        for (User u : userService.getAll()) {
            users.put(u.getId(), u);
        }

        for (Proposal p : proposals) {
            p.setUser(users.get(p.getUser().getId()));
        }
*/
        return proposals;
    }

    public List<Proposal> getAllByUser(User user) {

        /*List<Proposal> orders = new ArrayList<>();

        for (Proposal o : proposalDao.findAll()) {
            if (o.getUser().getId() == user.getId()) {
                o.setUser(user);
                orders.add(o);
            }
        }*/
        return proposalRepo.findAllByUser(user);
    }

    @Transactional
    public Optional<Proposal> save(LocalDate arrival, LocalDate departure, int guests, Category category, User user) {
        Proposal proposal = new Proposal(arrival, departure, guests, category);
        proposal.setRegistered(LocalDateTime.now());
        proposal.setStatus(Proposal.Status.NEW);
        proposal.setUser(user);
        return Optional.of(proposalRepo.save(proposal));
    }

    @Transactional
    public Optional<Proposal> update(Proposal proposal) {
        return Optional.of(proposalRepo.save(proposal));
    }
/*
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

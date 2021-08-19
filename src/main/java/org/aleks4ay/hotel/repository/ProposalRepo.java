package org.aleks4ay.hotel.repository;

import org.aleks4ay.hotel.model.Proposal;
import org.aleks4ay.hotel.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepo extends CrudRepository<Proposal, Long> {
    List<Proposal> findAllByUser(User user);

}

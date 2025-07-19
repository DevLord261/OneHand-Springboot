package org.devlord.onehand.Campaign;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<CampaignEntity, UUID> {
    List<CampaignEntity> findByOrganizerId(UUID id);
    List<CampaignEntity> findByFeatured(boolean isfeatured);
}

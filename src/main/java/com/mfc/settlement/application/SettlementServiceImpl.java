package com.mfc.settlement.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.settlement.common.entity.SettlementRequestStatus;
import com.mfc.settlement.domain.SettlementRequest;
import com.mfc.settlement.dto.response.SettlementResult;
import com.mfc.settlement.infrastructure.SettlementRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {
	private final SettlementRequestRepository settlementRequestRepository;

	@Transactional
	@Override
	public void settlePartner(List<? extends SettlementResult> settlementResults) {
		settlementResults.forEach(settlementResult -> {
			Long settlementRequestId = settlementResult.getId();
			SettlementRequest settlementRequest = settlementRequestRepository.findById(settlementRequestId)
				.orElse(null);

			if (settlementRequest != null) {
				SettlementRequest updatedSettlementRequest = SettlementRequest.builder()
					.id(settlementRequest.getId())
					.partnerId(settlementRequest.getPartnerId())
					.amount(settlementRequest.getAmount())
					.status(SettlementRequestStatus.COMPLETED)
					.build();

				settlementRequestRepository.save(updatedSettlementRequest);
			} else {
				System.out.println("Settlement request not found for id: " + settlementRequestId);
			}
		});
	}
}
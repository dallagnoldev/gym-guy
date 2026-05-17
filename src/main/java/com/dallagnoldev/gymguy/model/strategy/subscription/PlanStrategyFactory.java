package com.dallagnoldev.gymguy.model.strategy.subscription;

import com.dallagnoldev.gymguy.model.enums.UserPlanTypeEnum;
import com.dallagnoldev.gymguy.model.strategy.subscription.limits.FreePlanStrategy;
import com.dallagnoldev.gymguy.model.strategy.subscription.limits.ISubscriptionLimitStrategy;
import com.dallagnoldev.gymguy.model.strategy.subscription.limits.PremiumPlanStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanStrategyFactory {

    private final FreePlanStrategy freePlan;
    private final PremiumPlanStrategy premiumPlan;

    public ISubscriptionLimitStrategy getStrategy(UserPlanTypeEnum planType) {
        if (planType == null) {
            return freePlan;
        }

        return switch(planType) {
            case FREE -> freePlan;
            case PREMIUM -> premiumPlan;
        };
    }
}

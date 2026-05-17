package com.dallagnoldev.gymguy.model.strategy.subscription.limits;

import org.springframework.stereotype.Component;

@Component
public class PremiumPlanStrategy implements ISubscriptionLimitStrategy{
    @Override
    public int getMaxWorkouts() {
        return 20;
    }

    @Override
    public int getMaxCustomExercises() {
        return 100;
    }

    @Override
    public boolean canCreateMoreWorkouts(long currentCount) {
        return currentCount <= getMaxWorkouts();
    }

    @Override
    public boolean canCreateMoreCustomExercises(long currentCount) {
        return currentCount <= getMaxCustomExercises();
    }
}

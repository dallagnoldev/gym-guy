package com.dallagnoldev.gymguy.model.strategy.subscription.limits;

public interface ISubscriptionLimitStrategy {
    int getMaxWorkouts();
    int getMaxCustomExercises();
    boolean canCreateMoreWorkouts(long currentCount);
    boolean canCreateMoreCustomExercises(long currentCount);
}

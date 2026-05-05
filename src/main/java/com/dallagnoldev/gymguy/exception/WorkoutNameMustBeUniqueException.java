package com.dallagnoldev.gymguy.exception;

public class WorkoutNameMustBeUniqueException extends Exception {
    public WorkoutNameMustBeUniqueException(String message) {
        super(message);
    }
}

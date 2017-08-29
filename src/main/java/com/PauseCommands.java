package com;

public enum PauseCommands {
    newGame("Новая игра"), continueGame("Продолжить"), exitGame("Выход");

    private String text;

    PauseCommands(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

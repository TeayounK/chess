package websocket.commands;

import java.util.Objects;

public class CandM {
    private final Move move;
    private final UserGameCommand userGameCommand;

    public CandM(UserGameCommand command, Move move) {
        this.move=move;
        this.userGameCommand=command;
    }

    public Move getMove(){
        return this.move;
    }
    public UserGameCommand getUserGameCommand(){
        return this.userGameCommand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CandM candM = (CandM) o;
        return Objects.equals(move, candM.move) && Objects.equals(userGameCommand, candM.userGameCommand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(move, userGameCommand);
    }
}

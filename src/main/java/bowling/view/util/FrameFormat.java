package bowling.view.util;

import bowling.domain.framescore.*;
import bowling.domain.score.Score;
import bowling.domain.score.TurnScores;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

public class FrameFormat {
    private static final char TURN_SCORE_DELIMITER = '|';

    private final FrameScore frameScore;

    public FrameFormat(final FrameScore frameScore) {
        this.frameScore = frameScore;
    }

    public String format() {
        if (frameScore instanceof FinalFrameScore) {
            return toDisplayFinalFrameScore((FinalFrameScore) frameScore);
        }
        if (frameScore instanceof Miss) {
            return Text.MISS.toString();
        }
        if (frameScore instanceof Strike) {
            return Text.STRIKE.toString();
        }
        if (frameScore instanceof Spare) {
            return Text.SPARE.format(toDisplayFirstTurnScore(frameScore));
        }
        return toDisplayTurnScores(frameScore.turnScores());
    }

    private String toDisplayFirstTurnScore(FrameScore frameScore) {
        return scoreFormat(frameScore.turnScores().first());
    }

    private String toDisplayFinalFrameScore(FinalFrameScore finalFrameScore) {
        if (finalFrameScore.isSpare()) {
            int firstValue = frameScore.turnScores().first().value();
            return finalFrameScore.isCompleted() ?
                    Text.FINAL_SPARE.format(firstValue, frameScore.turnScores().last().value()) :
                    Text.SPARE.format(firstValue);
        }
        return toDisplayTurnScores(frameScore.turnScores());
    }

    private String toDisplayTurnScores(TurnScores turnScores) {
        List<String> displayScores = new ArrayList<>();
        turnScores.forEach(
                iTurnScore ->
                        displayScores.add(
                                scoreFormat(iTurnScore)
                        )
        );

        return Strings.join(displayScores, TURN_SCORE_DELIMITER);
    }

    private String scoreFormat(Score score) {
        return new ScoreFormat(score).format();
    }

    private enum Text {
        MISS("-"),
        STRIKE("X"),
        SPARE("%s|/"),
        FINAL_SPARE("%s|/|%s");

        private final String str;

        Text(String str) {
            this.str = str;
        }

        public String format(Object... objs) {
            return String.format(str, objs);
        }

        @Override
        public String toString() {
            return str;
        }
    }
}
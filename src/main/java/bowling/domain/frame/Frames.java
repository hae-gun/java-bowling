package bowling.domain.frame;

import bowling.domain.frame.exception.InvalidFrameRecordActionException;
import bowling.domain.pin.Pin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Frames {
    private static final int MIN_FRAME_NUMBER = 1;
    public static final int MAX_FRAME_NUMBER = 10;
    private final List<Frame> frames;
    private Integer currentFrameNumber;

    private Frames(List<Frame> frames) {
        this.frames = frames;
        this.currentFrameNumber = 1;
    }

    public static Frames create() {
        return new Frames(createFrames());
    }

    private static List<Frame> createFrames() {
        return IntStream.range(MIN_FRAME_NUMBER, MAX_FRAME_NUMBER + 1)
                .mapToObj(Frames::createFrame).collect(Collectors.toList());
    }

    private static Frame createFrame(int frameNumber) {
        if (frameNumber == MAX_FRAME_NUMBER) {
            return LastFrame.empty();
        }
        return Frame.empty();
    }

    public static Frames of(List<Frame> frames) {
        return new Frames(frames);
    }

    public int getCurrentFrameNumber() {
        return currentFrameNumber;
    }

    public boolean isFinished() {
        return frames.stream()
                .allMatch(Frame::isFinished);
    }

    public boolean record(Pin pins) {
        validateRecordPossible();
        Frame currentFrame = getFrame(currentFrameNumber);
        if (currentFrame.record(pins)) {
            currentFrameNumber += 1;
            return true;
        }
        return false;
    }

    private void validateRecordPossible() {
        if (isFinished()) {
            throw new InvalidFrameRecordActionException();
        }
    }

    public List<Frame> getFrames() {
        return Collections.unmodifiableList(frames);
    }

    public List<Integer> calculateScores() {
        List<Integer> calculatedScores = new ArrayList<>();
        Integer previousFrameScore = 0;
        for (int frameNumber = MIN_FRAME_NUMBER; frameNumber <= MAX_FRAME_NUMBER; frameNumber++) {
            previousFrameScore = calculateScore(frameNumber, previousFrameScore);
            calculatedScores.add(previousFrameScore);
        }
        return calculatedScores;
    }

    private Integer calculateScore(int frameNumber, Integer previousFrameScore) {
        return Optional.ofNullable(previousFrameScore)
                .map(previousScore -> getFrame(frameNumber).calculateScore(previousScore, getNextFrames(frameNumber)))
                .orElse(null);
    }

    private List<Frame> getNextFrames(int frameNumber) {
        return frames.subList(frameNumber, Math.min(frameNumber + 2, MAX_FRAME_NUMBER));
    }

    private Frame getFrame(int frameNumber) {
        return frames.get(frameNumber - 1);
    }
}
package bowling.domain.state.running;

import bowling.domain.pin.Pins;
import bowling.domain.state.State;

import java.util.Collections;
import java.util.List;

public abstract class Running implements State {

    @Override
    public boolean isFinish() {
        return false;
    }

    @Override
    public boolean isMiss() {
        return false;
    }

    @Override
    public boolean isCleanState() {
        return false;
    }

    public Pins getFirstPins() {
        throw new IllegalArgumentException("첫 번째 투구에 대한 볼링 핀 정보가 없습니다.");
    }

    public Pins getSecondPins() {
        throw new IllegalArgumentException("두 번째 투구에 대한 볼링 핀 정보가 없습니다.");
    }

    @Override
    public List<State> getState() {
        return Collections.singletonList(this);
    }
}

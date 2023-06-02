package mind.box.fx.service;

@FunctionalInterface
public interface Message {

    void onMessage(String message);
}

package Observer_model;

public interface Observer {
    void update(String message);

    String getReferenceCode();

    String getName();

    int getPriority();
}

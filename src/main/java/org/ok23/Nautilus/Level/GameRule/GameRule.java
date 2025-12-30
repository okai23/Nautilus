package Level.GameRule;

public class GameRule<T>
{
    private final String label;
    private final T value;

    public GameRule(String label, T value)
    {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public T getValue() { return value; }
}

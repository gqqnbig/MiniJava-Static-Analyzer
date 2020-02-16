public interface FlowSensitiveVariable<TInput> extends Variable<TInput>
{
	Location getStatement();
}

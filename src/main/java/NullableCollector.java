//
//public class NullableCollector extends ScopeVisitor<Object>
//{
//	public static boolean isNullable(Type type)
//	{
//		if (type.f0.choice instanceof BooleanType)
//			return false;
//		if (type.f0.choice instanceof IntegerType)
//			return false;
//
//		assert type.f0.choice instanceof Identifier || type.f0.choice instanceof ArrayType;
//		return true;
//	}
//
//	public static void init(Goal goal)
//	{
//		goal.accept(new NullableCollector());
//	}
//
//
////	static HashMap<NullableIdentifier, Scope> nullables = new HashMap<>();
//
//	static ArrayList<ObjectIdentifierDefinition> nullables = new ArrayList<>();
//
//	/*
//	map from subclass to its superclass
//	 */
//	static HashMap<String, String> superClassHierarchy = new HashMap<>();
//
//	private static List<ObjectIdentifierDefinition> getNullableFieldsDefinedInClass(String className)
//	{
//		List<ObjectIdentifierDefinition> scopeNullables = new ArrayList<>();
//		for (ObjectIdentifierDefinition entry : nullables)
//		{
//			if (entry.Class.equals(className) && entry.Method == null)
//				scopeNullables.add(entry);
//		}
//		return scopeNullables;
//	}
//
//	public static List<ObjectIdentifierDefinition> getNullableIdentifiersInScope(Scope scope)
//	{
//		List<ObjectIdentifierDefinition> scopeNullables = new ArrayList<>();
//		for (ObjectIdentifierDefinition entry : nullables)
//		{
//			if (entry.Class.equals(scope.Class) &&
//					(entry.Method == null || entry.Method.equals(scope.Method))) //fields are available in a method.
//				scopeNullables.add(entry);
//		}
//
//		String className = superClassHierarchy.get(scope.Class);
//		while (className != null)
//		{
//			scopeNullables.addAll(getNullableFieldsDefinedInClass(className));
//			className = superClassHierarchy.get(className);
//		}
//
//		return scopeNullables;
//	}
//
//	/**
//	 * Get definition of a nullable identifier from the identifier usage.
//	 * <p>
//	 * If the identifier is not nullable, eg. int, return null.
//	 *
//	 * @param identifier
//	 * @return
//	 */
//	public static ObjectIdentifierDefinition getDefinition(Identifier identifier, Scope scope)
//	{
//		ObjectIdentifierDefinition fieldDefinition = null;
//		for (var d : getNullableIdentifiersInScope(scope))
//		{
//			if (d.getIdentifier().equals(identifier.f0.toString()))
//			{
//				if (d.Method == null)
//				{
//					assert fieldDefinition == null;
//					fieldDefinition = d;
//				}
//				else
//					return d;
//			}
//		}
//		return fieldDefinition;
//	}
//
////	public static List<NullableIdentifier> getNullableIdentifierInScope(Scope scope)
////	{
////		List<NullableIdentifier> scopeNullables = new ArrayList<>();
////		for (HashMap.Entry<NullableIdentifier, Scope> entry : nullables.entrySet())
////		{
////			if (entry.getValue().equals(scope))
////				scopeNullables.add(entry.getKey());
////		}
////		return scopeNullables;
////	}
////
////	static HashMap<Identifier, Scope> nullables = new HashMap<>();
//
//
//	private NullableCollector() {}
//
//	@Override
//	public Object visitScope(MainClass n)
//	{
//		n.f14.accept(this);
//		return null;
//	}
//
//	@Override
//	public Object visitScope(MethodDeclaration n)
//	{
//		n.f4.accept(this);
//		n.f7.accept(this);
//		return null;
//	}
//
//	@Override
//	public Object visitScope(ClassDeclaration n)
//	{
//		n.f3.accept(this);
//		n.f4.accept(this);
//		return null;
//	}
//
//	@Override
//	protected Object visitScope(ClassExtendsDeclaration n)
//	{
//		superClassHierarchy.put(n.f1.f0.toString(), n.f3.f0.toString());
//		n.f5.accept(this);
//		n.f6.accept(this);
//		return null;
//	}
//
//	@Override
//	public Object visit(VarDeclaration n)
//	{
//		if (isNullable(n.f0))
//		{
////			nullables.put(new NullableIdentifier(n.f1), new Scope(getClassName(), getMethodName()));
//			nullables.add(new ObjectIdentifierDefinition(n.f1, getClassName(), getMethodName(), false));
//		}
//		return null;
//	}
//
//	@Override
//	public Object visit(FormalParameter n)
//	{
//		if (isNullable(n.f0))
//		{
////			nullables.put(new NullableIdentifier(n.f1), new Scope(getClassName(), getMethodName()));
//			nullables.add(new ObjectIdentifierDefinition(n.f1, getClassName(), getMethodName(), true));
//		}
//		return null;
//	}
//}

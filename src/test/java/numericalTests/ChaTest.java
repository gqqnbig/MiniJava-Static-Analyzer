import numericalAnalysis.ProgramStructureCollector;
import org.junit.Assert;
import org.junit.Test;
import syntaxtree.*;
import typeAnalysis.ClassHierarchyAnalysis;
import visitor.GJNoArguDepthFirst;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Enumeration;

public class ChaTest
{
	@Test
	public void testThisMethod() throws FileNotFoundException, ParseException
	{
		FileInputStream stream = new FileInputStream("testcases/hw3/ThisNameTest.java");
		try {MiniJavaParser.ReInit(stream);} catch (Throwable e) {new MiniJavaParser(stream);}
		Goal goal = MiniJavaParser.Goal();


		ProgramStructureCollector.init(goal);
		ClassHierarchyAnalysis.init(goal);

		PrimaryExpression thisExpression = goal.accept(new ThisFinder());

		Collection<String> types = ClassHierarchyAnalysis.getPossibleTypes(thisExpression,"A" , "f", 0);

		Assert.assertEquals(5, types.size());
		Assert.assertTrue(types.contains("A"));
		Assert.assertTrue(types.contains("C"));
		Assert.assertTrue(types.contains("D"));
		Assert.assertTrue(types.contains("E"));
		Assert.assertTrue(types.contains("F"));

	}


	static class ThisFinder extends GJNoArguDepthFirst<PrimaryExpression>
	{
		@Override
		public PrimaryExpression visit(MainClass n)
		{
			return null;
		}

		@Override
		public PrimaryExpression visit(MethodDeclaration n)
		{
			PrimaryExpression result = visit(n.f8);
			if (result != null)
				return result;
			return visit(n.f10);
		}

		public PrimaryExpression visit(MessageSend n)
		{
			PrimaryExpression result = n.f0.accept(this);
			if (result != null)
				return result;

			result = n.f4.accept(this);
			if (result != null)
				return result;
			return null;
		}

		public PrimaryExpression visit(Expression n)
		{
			return n.f0.accept(this);
		}

		public PrimaryExpression visit(NodeListOptional n)
		{
			if (n.present())
			{
				for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
				{
					PrimaryExpression _ret = e.nextElement().accept(this);
					if (_ret != null)
						return _ret;
				}
				return null;
			}
			else
				return null;
		}

		public PrimaryExpression visit(ClassDeclaration n)
		{
			return n.f4.accept(this);
		}

		public PrimaryExpression visit(TypeDeclaration n)
		{
			return n.f0.accept(this);
		}

		public PrimaryExpression visit(Goal n)
		{
			return n.f1.accept(this);
		}

		public PrimaryExpression visit(PrimaryExpression n)
		{
			if (n.f0.choice instanceof ThisExpression)
				return n;
			else
				return super.visit(n);
		}
	}
}

package me.tuple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Created by gokul-4192.
 */
public class ArrayListTest
{
	@State(Scope.Thread)
	public static class Constants
	{
		private static final int NO_OF_VALS = 50;
		private static final ArrayList<Integer> vals = new ArrayList<>(NO_OF_VALS);

		public Constants()
		{
			if (vals.isEmpty())
			{
				Random random = new Random();
				for (int i = 0; i < NO_OF_VALS; i++)
				{
					vals.add(i + random.nextInt(Integer.MAX_VALUE / 2));
				}
			}
		}
	}

	@Benchmark
	@BenchmarkMode({Mode.SingleShotTime})
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void forEachList(Constants constants, Blackhole blackhole)
	{
		ArrayList<Integer> vals = constants.vals;
		for (Integer val : vals)
		{
			blackhole.consume(val);
		}
	}


	@Benchmark
	@BenchmarkMode({Mode.SingleShotTime})
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void forEachIndexed(Constants constants, Blackhole blackhole)
	{
		ArrayList<Integer> vals = constants.vals;
		for (int i = 0; i < vals.size(); i++)
		{
			blackhole.consume(vals.get(i));
		}
	}


	@Benchmark
	@BenchmarkMode({Mode.SingleShotTime})
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void forEachFastIndexed(Constants constants, Blackhole blackhole)
	{
		ArrayList<Integer> vals = constants.vals;
		for (int i = 0, size = vals.size(); i < size; i++)
		{
			blackhole.consume(vals.get(i));
		}
	}

	@Benchmark
	@BenchmarkMode({Mode.SingleShotTime})
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void iterator(Constants constants, Blackhole blackhole)
	{
		Iterator<Integer> itr = constants.vals.iterator();
		while (itr.hasNext())
		{
			blackhole.consume(itr.next());
		}
	}

	@Benchmark
	@BenchmarkMode({Mode.SingleShotTime})
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void listIterator(Constants constants, Blackhole blackhole)
	{
		ListIterator<Integer> itr = constants.vals.listIterator();
		while (itr.hasNext())
		{
			blackhole.consume(itr.next());
		}
	}


	@Benchmark
	@BenchmarkMode({Mode.SingleShotTime})
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void add(Constants constants, Blackhole blackhole)
	{
		ArrayList<Integer> vals = new ArrayList<>();
		for (int i = 0; i < Constants.NO_OF_VALS; i++)
		{
			vals.add(i);
		}
		blackhole.consume(vals);
	}

	@Benchmark
	@BenchmarkMode({Mode.SingleShotTime})
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void preparedAdd(Constants constants, Blackhole blackhole)
	{
		ArrayList<Integer> vals = new ArrayList<>(Constants.NO_OF_VALS);
		for (int i = 0; i < Constants.NO_OF_VALS; i++)
		{
			vals.add(i);
		}
		blackhole.consume(vals);
	}

	public static void main(String[] args) throws RunnerException
	{
		Options opt = new OptionsBuilder()
			   .include(".*" + ArrayListTest.class.getSimpleName() + ".*")
			   .warmupIterations(5)
			   .build();
		new Runner(opt).run();
	}
}

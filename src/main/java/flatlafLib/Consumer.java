package flatlafLib;

import com.formdev.flatlaf.FlatDarkLaf;

public class Consumer
	extends FlatDarkLaf
{
	public static final String NAME = "Consumer";

	public static boolean setup() {
		return setup( new Consumer() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Consumer.class );
	}

	@Override
	public String getName() {
		return NAME;
	}
}

package net.sprintasia.bayarind.lib;


import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Helper {

	public static boolean luhnCheck(String ccNumber)
    {
		int sum = 0;
		boolean alternate = false;
		for (int i = ccNumber.length() - 1; i >= 0; i--)
		{
			int n = Integer.parseInt(ccNumber.substring(i, i + 1));
			if (alternate)
			{
				n *= 2;
				if (n > 9)
				{
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		return (sum % 10 == 0);
    }

	public static void selectSpinnerItemByValue(Spinner spnr, String value) {
		ArrayAdapter adapter = (ArrayAdapter) spnr.getAdapter();
		for (int position = 0; position < adapter.getCount(); position++) {
			if(adapter.getItem(position).toString().equalsIgnoreCase(value)) {
				spnr.setSelection(position);
				return;
			}
		}
	}

	public static String formatDecimal(int number) {
	    double n = (double) number;
		return String.format("%,.2f", n);
	}
}


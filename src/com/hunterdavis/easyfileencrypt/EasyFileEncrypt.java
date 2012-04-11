package com.hunterdavis.easyfileencrypt;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class EasyFileEncrypt extends Activity {

	private static final AlgorithmParameterSpec AES_Key_Size = null;

	int SELECT_FILE = 122;

	String filePath = "";
	String password = "";
	int SELECT_MULTI_FILE = 223;
	
	static long fileSizeInBytes = 0;
	private Context magicContext;
	public Vector magicNamesVector;
    public Boolean lastEncrypt = false;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		lastEncrypt = false;
		magicNamesVector = new Vector();

		// Create an anonymous implementation of OnClickListener
		OnClickListener loadButtonListner = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked

				// in onCreate or any event where your want the user to
				Intent intent = new Intent(v.getContext(), FileDialog.class);
				intent.putExtra(FileDialog.START_PATH, "/sdcard");
				startActivityForResult(intent, SELECT_FILE);

			}
		};

		// Create an anonymous implementation of OnClickListener
		OnClickListener saveButtonListner = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked
				AlertDialog.Builder alert = new AlertDialog.Builder(
						v.getContext());

				alert.setTitle("Password");
				alert.setMessage("Please Enter A Password to Encrypt File");

				// Set an EditText view to get user input
				final EditText input = new EditText(v.getContext());
				alert.setView(input);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String tempName = input.getText().toString();

								if (tempName.length() > 0) {
									password = tempName;
									scrambleFile(false);
								} else {
									Toast.makeText(getBaseContext(),
											"Invalid Password!",
											Toast.LENGTH_LONG).show();
								}
							}

						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});

				alert.show();

			}
		};

		// Create an anonymous implementation of OnClickListener
		OnClickListener decryptButtonListener = new OnClickListener() {
			public void onClick(View v) {
				magicNamesVector.clear();
				// do something when the button is clicked
				AlertDialog.Builder alert = new AlertDialog.Builder(
						v.getContext());

				alert.setTitle("Password");
				alert.setMessage("Please Enter A Password to Decrypt File");

				// Set an EditText view to get user input
				final EditText input = new EditText(v.getContext());
				alert.setView(input);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String tempName = input.getText().toString();

								if (tempName.length() > 0) {
									password = tempName;
									scrambleFile(true);
								} else {
									Toast.makeText(getBaseContext(),
											"Invalid Password!",
											Toast.LENGTH_LONG).show();
								}
							}

						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});

				alert.show();

			}
		};
		
		
		// Create an anonymous implementation of OnClickListener
		OnClickListener multiEncryptButtonListner = new OnClickListener() {
			public void onClick(View v) {
				magicNamesVector.clear();
				lastEncrypt = true;
				magicNamesVector.clear();
				magicContext = v.getContext();
				// in onCreate or any event where your want the user to
				Intent intent = new Intent(v.getContext(), FileDialog.class);
				intent.putExtra(FileDialog.START_PATH, "/sdcard");
				startActivityForResult(intent, SELECT_MULTI_FILE);
			}
		};
		
		// Create an anonymous implementation of OnClickListener
		OnClickListener multiDecryptButtonListner = new OnClickListener() {
			public void onClick(View v) {
				magicNamesVector.clear();
				lastEncrypt = false;
				magicNamesVector.clear();
				magicContext = v.getContext();
				// in onCreate or any event where your want the user to
				Intent intent = new Intent(v.getContext(), FileDialog.class);
				intent.putExtra(FileDialog.START_PATH, "/sdcard");
				startActivityForResult(intent, SELECT_MULTI_FILE);
			}
		};

		Button meButton = (Button) findViewById(R.id.multiencryptbutton);
		meButton.setOnClickListener(multiEncryptButtonListner);

		Button mdButton = (Button) findViewById(R.id.multidecryptbutton);
		mdButton.setOnClickListener(multiDecryptButtonListner);


		Button loadButton = (Button) findViewById(R.id.loadButton);
		loadButton.setOnClickListener(loadButtonListner);

		Button saveButton = (Button) findViewById(R.id.encryptbutton);
		saveButton.setOnClickListener(saveButtonListner);

		Button decryptButton = (Button) findViewById(R.id.decryptbutton);
		decryptButton.setOnClickListener(decryptButtonListener);

		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

	}
	

	public void scrambleFile(Boolean descramble) {

		// get a handle to the name
		// read in .old file
		// write out file
		// delete .old file?

		// rename file to .old
		String fileString = filePath;
		File oldfile = new File(fileString);
		File newfile = new File(fileString + ".old");
		oldfile.renameTo(newfile);


		// now try to open the first output file

		
		if (descramble == true) {

			SimpleCrypto crypto = new SimpleCrypto();
			try {
				Boolean madeit = crypto.decryptFile(newfile,oldfile,password);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			newfile.delete();
			Toast.makeText(getBaseContext(), "Decrypted " + fileString,
					Toast.LENGTH_SHORT).show();
		} else {
			SimpleCrypto crypto  = new SimpleCrypto();
			try {
				Boolean madeit = crypto.encryptFile(newfile,oldfile,password);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			newfile.delete();
			Toast.makeText(getBaseContext(), "Encrypted " + fileString,
					Toast.LENGTH_SHORT).show();
		}

	}

	public String getFileName() {
		int slashloc = filePath.lastIndexOf("/");
		if (slashloc < 0) {
			return filePath;
		} else {
			return filePath.substring(slashloc + 1);
		}
	}

	public void onActivityResult(final int requestCode, int resultCode,
			final Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_FILE) {
				filePath = data.getStringExtra(FileDialog.RESULT_PATH);
				// set the filename txt
				changeFileNameText(filePath);
				Button enabButton = (Button) findViewById(R.id.encryptbutton);
				enabButton.setEnabled(true);
				enabButton = (Button) findViewById(R.id.decryptbutton);
				enabButton.setEnabled(true);
			}
			else if(requestCode == SELECT_MULTI_FILE) {
				filePath = data.getStringExtra(FileDialog.RESULT_PATH);
				askForAnother(filePath);
			}
		} else if (resultCode == RESULT_CANCELED) {
		}
	}
	
	public void askForAnother(String newPath) {
		magicNamesVector.add(newPath);

		AlertDialog.Builder alert = new AlertDialog.Builder(magicContext);
		alert.setTitle("Add Another?");
		alert.setMessage("You Just Added " + newPath + ", which makes "
				+ magicNamesVector.size() + " items, add another?");

		// Set an EditText view to get user input
		// final EditText input = new EditText(context);
		// input.setInputType(InputType.TYPE_CLASS_NUMBER);
		// input.setText("1");
		// alert.setView(input);

		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(magicContext, FileDialog.class);
				intent.putExtra(FileDialog.START_PATH, "/sdcard");
				startActivityForResult(intent, SELECT_MULTI_FILE);
			}

		});

		alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				askToJoin();
			}
		});

		alert.show();

	}
	
	public void askToJoin() {

		AlertDialog.Builder alert = new AlertDialog.Builder(magicContext);
		String choiseString = "";
		if(lastEncrypt == true) {
			choiseString = "Encrypt";
		}
		else {
			choiseString = "Decrypt";
		}
		
		alert.setTitle(choiseString +" Files?");
		alert.setMessage(choiseString + " The Following Files?");

		// Set an EditText view to get user input
		final EditText input = new EditText(magicContext);
		input.setEnabled(false);
		input.setLines(10);
		input.setVerticalScrollBarEnabled(true);

		String alertBoxString = "";

		for (int i = 0; i < magicNamesVector.size(); i++) {
			try {
				alertBoxString += magicNamesVector.get(i) + "\n";
			} catch (Exception e) {

			}
		}
		input.setText(alertBoxString);
		alert.setView(input);

		alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				magicJoin();
				dialog.dismiss();
			}

		});

		alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				magicNamesVector.clear();
			}
		});

		alert.show();

	}


	public void changeFileNameText(String newFileName) {
		TextView t = (TextView) findViewById(R.id.fileText);
		t.setText(newFileName);
	}
	
	public void magicJoin() {
		// do something when the button is clicked
		AlertDialog.Builder alert = new AlertDialog.Builder(
				magicContext);

		alert.setTitle("Password");
		alert.setMessage("Please Enter A Password For Files");

		// Set an EditText view to get user input
		final EditText input = new EditText(magicContext);
		alert.setView(input);

		alert.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						String tempName = input.getText().toString();

						if (tempName.length() > 0) {
							password = tempName;
							magicJoinStageTwo();
						} else {
							Toast.makeText(getBaseContext(),
									"Invalid Password!",
									Toast.LENGTH_LONG).show();
						}
					}

				});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						// Canceled.
						magicNamesVector.clear();
					}
				});

		alert.show();
	}
	
	public void magicJoinStageTwo() {
		for(int i = 0;i < magicNamesVector.size();i++) {
			try {
				filePath = (String) magicNamesVector.get(i);
			} catch (Exception e) {
				return;
			}
			if(lastEncrypt == true) {
				scrambleFile(false);
			}
			else {
				scrambleFile(true);
			}
		}
	}

}
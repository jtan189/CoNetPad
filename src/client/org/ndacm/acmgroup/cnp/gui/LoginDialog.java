package org.ndacm.acmgroup.cnp.gui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFormattedTextField;

import org.ndacm.acmgroup.cnp.CNPClient;

public class LoginDialog extends JDialog {
	private JButton btnRegister;
	private LoginDialog loginDialog;
	private CNPClient client;

	/**
	 * Create the dialog.
	 */
	public LoginDialog(final CNPClient client) {
		loginDialog = this;
		this.client = client;
		this.client.setLogDialog(this);
		setTitle("CoNetPad Client");
		setBounds(100, 100, 418, 199);
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		JLabel lblUsername = new JLabel("Username:");
		JLabel lblPassword = new JLabel("Password:");

		JLabel lblSession = new JLabel("Session:");

		final JFormattedTextField formattedUsername = new JFormattedTextField();

		final JFormattedTextField formattedPassword = new JFormattedTextField();

		JFormattedTextField formattedSession = new JFormattedTextField();

		JLabel lblSessionToJoin = new JLabel(
				"If you don't have a session, input a name for your new session");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 402,
								Short.MAX_VALUE)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																lblPassword)
														.addComponent(
																lblUsername))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																formattedUsername,
																GroupLayout.DEFAULT_SIZE,
																320,
																Short.MAX_VALUE)
														.addComponent(
																formattedPassword,
																GroupLayout.DEFAULT_SIZE,
																320,
																Short.MAX_VALUE))
										.addContainerGap())
						.addGroup(
								groupLayout.createSequentialGroup()
										.addContainerGap()
										.addComponent(lblSessionToJoin)
										.addContainerGap(257, Short.MAX_VALUE))
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(lblSession)
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(formattedSession,
												GroupLayout.DEFAULT_SIZE, 332,
												Short.MAX_VALUE)
										.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblUsername)
														.addComponent(
																formattedUsername,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGap(6)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblPassword)
														.addComponent(
																formattedPassword,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(lblSessionToJoin)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblSession)
														.addComponent(
																formattedSession,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED, 42,
												Short.MAX_VALUE)
										.addComponent(panel,
												GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)));
		{
			btnRegister = new JButton("Register");
			btnRegister.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RegisterDialog dialog = new RegisterDialog(client);
					dialog.setModalityType(ModalityType.APPLICATION_MODAL);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
		}
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.closeConnection();
				dispose();
			}
		});
		JButton btnLogIn = new JButton("Log In");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.loginToAccount(formattedUsername.getText(),
						formattedPassword.getText());
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnRegister)
						.addPreferredGap(ComponentPlacement.RELATED, 181,
								Short.MAX_VALUE).addComponent(btnLogIn)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnCancel).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING)
				.addGroup(
						Alignment.TRAILING,
						gl_panel.createSequentialGroup()
								.addContainerGap(27, Short.MAX_VALUE)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(btnRegister)
												.addComponent(btnCancel)
												.addComponent(btnLogIn))
								.addContainerGap()));
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
	}

	public void openMainFrame() {
		MainFrame frame = new MainFrame(client);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		loginDialog.setVisible(false);
		loginDialog.dispose();

	}
}

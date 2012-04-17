package com.izforge.izpack.panels.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import com.izforge.izpack.api.container.BindeableContainer;
import com.izforge.izpack.api.data.AutomatedInstallData;
import com.izforge.izpack.api.handler.AbstractUIProcessHandler;
import com.izforge.izpack.api.rules.RulesEngine;
import com.izforge.izpack.api.substitutor.VariableSubstitutor;
import com.izforge.izpack.installer.console.PanelConsole;
import com.izforge.izpack.installer.console.PanelConsoleHelper;
import com.izforge.izpack.installer.container.impl.InstallerContainer.HasInstallerContainer;

public class ProcessPanelConsoleHelper extends PanelConsoleHelper implements PanelConsole, AbstractUIProcessHandler, HasInstallerContainer {

	private BindeableContainer installerContainer;
	private int noOfJobs = 0;
	private int currentJob = 0;

	@Override
	public boolean runGeneratePropertiesFile(AutomatedInstallData installData, PrintWriter printWriter) {
		return true;
	}

	@Override
	public boolean runConsoleFromProperties(AutomatedInstallData installData, Properties p) {
		return runConsole(installData);
	}

	@Override
	public boolean runConsole(AutomatedInstallData installData) {
		ProcessPanelWorker worker = createWorker(installData);
		worker.run();
		if (!worker.getResult()) throw new RuntimeException("The work done by the ProcessPanel failed");
		return true;
	}

	private ProcessPanelWorker createWorker(AutomatedInstallData installData) {
		if (installerContainer == null) throw new NullPointerException("Missing installer container");
		noOfJobs = currentJob = 0; //reset variables

		RulesEngine rules = installerContainer.getComponent(RulesEngine.class);
		VariableSubstitutor substitutor = installerContainer.getComponent(VariableSubstitutor.class);
		try {
			ProcessPanelWorker worker = new ProcessPanelWorker(installData, substitutor, rules);
			worker.setHandler(this);
			return worker;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void logOutput(String message, boolean stderr) {
		if (stderr) System.err.println(message);
		else System.out.println(message);
	}

	@Override
	public void startProcessing(int no_of_processes) {
		System.out.println("[ Starting processing ]");
		this.noOfJobs = no_of_processes;
	}

	@Override
	public void startProcess(String name) {
		this.currentJob++;
		System.out.println("Starting process " + name + " (" + this.currentJob + "/" + this.noOfJobs + ")");
	}

	@Override
	public void finishProcess() {
	}

	@Override
	public void finishProcessing(boolean unlockPrev, boolean unlockNext) {
		if (!unlockNext) throw new IllegalStateException("Process failed");
		System.out.println("[ Processing finished ]");
	}

	@Override
	public BindeableContainer getInstallerContainer() {
		return installerContainer;
	}

	@Override
	public void setInstallerContainer(BindeableContainer installerContainer) {
		this.installerContainer = installerContainer;
	}
}

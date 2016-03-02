import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

/**
	attach to the running process to load a agent.
**/
public class Attach extends Thread{
	private String pid;
	private String jar;
	private String agentArgs;
	
	public void run(){
		VirtualMachine vm = null;
		try {
			vm = VirtualMachine.attach(pid);
			vm.loadAgent(jar, agentArgs);
			vm.detach();
		} catch (AttachNotSupportedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AgentLoadException e) {
			e.printStackTrace();
		} catch (AgentInitializationException e) {
			e.printStackTrace();
		}finally{
			try {
				if(vm != null)
				vm.detach();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	/**
	 * pid:id of attached process
	 * jar:agent jar to load
	 * @param args
	 */
	public void parseParam(String[] args){
		if(args.length < 2){
			throw new RuntimeException("arges no enough......");
		}
		String pid = args[0];
		String jar = args[1];
		String agentArgs = "";
		for(int i = 2; i < args.length;i++){
			if(i > 2)
				agentArgs = agentArgs + " ";
			agentArgs += args[i];
		}
		this.pid = pid;
		this.jar = jar;
		this.agentArgs = agentArgs;
	}
	
	public static void main(String[] args){
		Attach attach = new Attach();
		attach.parseParam(args);
		attach.start();
	}

}

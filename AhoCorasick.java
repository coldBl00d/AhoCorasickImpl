import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class AhoCorasick {
	
	public static class Node {
		public char element; 
		public List<Integer> output; 
		public List<Node> children;
		public Node failNode;
		public Node parent; 
		
		public Node(char element, int o) {
			this.element = element; 
			this.output = new ArrayList<Integer>();
			if(o != -1)
				this.output.add(o);
			this.children = new ArrayList<Node>();
		}
		
		@Override
		public boolean equals(Object n) {
			// TODO Auto-generated method stub
			if(n instanceof Node) {
				if(((Node) n).element == this.element) {
					return true;
				}
			}
			return false;
		}

		public int childExist(Node n) {
			return this.children.indexOf(n);
		}
		
		
		public void addChild(Node n) {
			children.add(n);
		}
		
		public void addOutput(int i) {
			if(i!=-1)
				this.output.add(i);
		}
		
	}
	
	public static Node root = new AhoCorasick.Node('.',-1);
	
	public static void buildTrie(String[] dictionary, int[] output) {
		root.failNode = root;
		for(int i =0; i<dictionary.length; i++) {
			String cWord = dictionary[i];
			Node currentRoot = AhoCorasick.root;
			for(int j=0; j<cWord.length(); j++) {
				char cChar = cWord.charAt(j);
				int o=-1;
				if(j == cWord.length()-1) {
					o = i; 
				}
				Node newNode = new Node(cChar,o);
				int index = currentRoot.childExist(newNode);
				if ( index == -1) {
					//no child
					//System.out.println("Child : " +  newNode.element + " does not exist under current root "+currentRoot.element);
					//System.out.println("Adding it and making it current root... ");
					currentRoot.addChild(newNode);
					newNode.parent = currentRoot;
					currentRoot = newNode; 
				}else {
					//System.out.println("Child : " +  newNode.element + " does exist exist under current root "+currentRoot.element +", making it current root");
					currentRoot = currentRoot.children.get(index);
					currentRoot.addOutput(o);
				}
			}
		}
		
		//set up fail nodes
		//1. fail all 1st level to root
		
		Queue<Node> queue =new LinkedList<Node>();
		queue.add(root);
		while(!(queue.isEmpty())) {
			Node currentNode = queue.poll();
			if(currentNode.element == '.') {
				//1 fail all child to root
				for(Node n: currentNode.children) {
					n.failNode = currentNode; 
					queue.add(n);
				}
			}else {
				//go to fail of its parent and try to find itself 
				//if not found point to root; 
				for(Node n: currentNode.children) {
					Node failOfParent = n.parent.failNode;
					assert failOfParent != null; 
					int index = failOfParent.children.indexOf(n); 
					if( index != -1)
						n.failNode = failOfParent.children.get(index);
					else
						n.failNode = AhoCorasick.root;
					queue.add(n);
				}
			}
		}
		
		System.out.println("Done");
	}
	
	public static int process(String dna, int[] health,  int first, int last) {
		Node pointer= root;
		int index;
		int ho =0;
		for(int i =0; i<dna.length();) {
			char curChar = dna.charAt(i);
			System.out.println("Current char :" +curChar);
			index = pointer.children.indexOf(new Node(curChar, -1));
			if(index != -1) {
				System.out.println("Found "+curChar +" as child of "+ pointer.element);
				pointer = pointer.children.get(index);
				
				i++;
			}else {
				System.out.println("Failing on : "+pointer.element + "For "+ curChar);
				if(pointer.equals(root)) {
					System.out.println("Failing on : "+pointer.element + "For "+ curChar);
					i++;
				}
				pointer = pointer.failNode;
				
			}
			if(pointer.output.size()>0) {
				for(int ind: pointer.output) {
					System.out.println(ind);
					if(ind >= first && ind <= last) {
						ho+=health[ind];
					}
				}
			}
			
		}
		return ho;
	}
	
	public static void main(String[] arg ) {
		
		//String[] d = {"a", "ab", "bc","bbc","cd","a"};
		String[] d = {"a","b","c","aa","d","b"};
		
		//int[] o = {5,10,15,1,2,3};
		int[] o = {1,2,3,4,5,6};
		
		buildTrie(d,o);
		printChilds(root);
		//System.out.println(process("caaab", o, 1, 5));
		System.out.println(process("xyz", o, 0, 4));
		
	}
	
	public static void printChilds(Node n) {
		System.out.println("Parent: "+n.element);
		System.out.println("Children :");
		for(Node no: n.children) {
			System.out.print(no.element+"Fail:"+no.failNode.element+"  ");
			System.out.print( "Output Indexes (");
			for(int i : no.output)
			  System.out.print(i+" ");
			System.out.println(" )");
			System.out.println();
		}
		System.out.println();
		
		for(Node no: n.children)
			printChilds(no);
		
	}

}

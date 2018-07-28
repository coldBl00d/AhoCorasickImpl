import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class AhoCorasick {
	
	public static class Node {
		public char element; 
		public int[] output; 
		public Node[] children;
		public Node failNode;
		public Node parent;
		int oi;
		
		public Node(char element, int o, int maxOutput) {
			this.element = element; 
			this.output = new int[maxOutput];
			Arrays.fill(this.output, -1);
			this.oi = 0;
			if(o != -1 && this.output.length>0) {
				this.output[0]=o;
				this.oi++;
			}
				
			this.children = new Node[26];
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

		public Node childExist(Node n) {
			return this.children[n.element - 'a'];
		}
		
		
		public boolean addChild(Node n) {
			
			if(children[n.element - 'a'] == null) {
				children[n.element - 'a'] = n;
				n.parent = this;
				return true;
			}
			else 
				children[n.element - 'a'].addOutput(n.output[0]);
				return false;
		}
		
		public void addOutput(int i) {
			if(i!=-1) {
				this.output[oi]=i;
				oi++;
			}
				
		}
		
	}
	
	public static Node root = new AhoCorasick.Node('.',-1,1);
	
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
				Node newNode = new Node(cChar,o, output.length);
				if ( currentRoot.addChild(newNode)) {
					//no child
					System.out.println("Child : " +  newNode.element + " does not exist under current root "+currentRoot.element);
					System.out.println("Adding it and making it current root... ");
					currentRoot = newNode; 
				}else {
					System.out.println("Child : " +  newNode.element + " does exist exist under current root "+currentRoot.element +", making it current root");
					currentRoot = currentRoot.children[newNode.element - 'a'];;
					//currentRoot.addOutput(o);
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
					if(n == null) break;
					n.failNode = currentNode; 
					queue.add(n);
				}
			}else {
				//go to fail of its parent and try to find itself 
				//if not found point to root; 
				for(Node n: currentNode.children) {
					if(n==null) break;
					Node failOfParent = n.parent.failNode;
					assert failOfParent != null; 
					Node searchNode = failOfParent.childExist(n); 
					if( searchNode != null)
						n.failNode = searchNode;
					else
						n.failNode = AhoCorasick.root;
					queue.add(n);
				}
			}
		}
		
		System.out.println("Done");
	}
	
	public static int process(String dna, int[] health,  int first, int last) {
		Node pointer=root;
		Node index;
		int ho =0;
		for(int i =0; i<dna.length();) {
			char curChar = dna.charAt(i);
			System.out.println("Current char :" +curChar);
			index = pointer.childExist(new Node(curChar, -1, 0));
			if(index != null) {
				System.out.println("Found "+curChar +" as child of "+ pointer.element);
				pointer = index;
				i++;
			}else {
				if(pointer.equals(root)) {
					System.out.println("Failing on root "+pointer.element + "For "+ curChar);
					i++;
				}else {
					System.out.println("Failing on : "+pointer.element + "For "+ curChar);
				}
				pointer = pointer.failNode;
				
			}
			if(pointer.output.length>0) {
				for(int ind: pointer.output) {
					if(ind==-1) break;
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
		System.out.println(process("caaab", o, 1, 5));
		//System.out.println(process("xyz", o, 0, 4));
		
	}
	
	public static void printChilds(Node n) {
		System.out.println("Parent: "+n.element);
		System.out.println("Children :");
		for(Node no: n.children) {
			if(no == null) break;
			System.out.print(no.element+"Fail:"+no.failNode.element+"  ");
			System.out.print( "Output Indexes (");
			for(int i : no.output)
			  System.out.print(i+" ");
			System.out.println(" )");
			System.out.println();
		}
		System.out.println();
		
		for(Node no: n.children) {
			if(no ==null) break;
			printChilds(no);
		}
			
		
	}

}

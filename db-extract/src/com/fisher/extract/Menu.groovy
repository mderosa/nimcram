package com.fisher.extract;

import com.fisher.extract.config.*

class Menu {
	
	/**
	 * run :: Map 
	 */
	def run() {
		def configuration  = inputConfiguration();
		def projectId = inputProjectId();
		assert configuration != null, "configuration is required"
		assert projectId != null, "projectId is required"
		return [configuration: configuration, projectId: projectId]
	}
	
	/**
	 * inputConfiguration :: Config 
	 */
	def inputConfiguration() {
		println "Do you want to move data"
		println "1: from a legacy tracker db to a current tracker db?"
		println "2: between two current tracker dbs?"
		println "3: from a legacy tracker db to a Project.net db?"
		println "4: Quit"
		print "('1','2', '3' or '4'):"
		def configuration
		def scan = new Scanner(System.in)
		try {
			switch (scan.nextInt()) {
				case 1:
					configuration = new Config2To3()
					break;
				case 2:
					configuration = new Config3To3()
					break;
				case 3:
					configuration = new Config2ToPnet()
					break;
				case 4:
					System.exit(0);
				default:
					println "Invalid input"
					configuration = inputConfiguration()
			}
		} catch (Exception e) {
			println e.getMessage()
			println "Please input ('1','2', or '3'):"
			configuration = inputConfiguration()
		}
		configuration.assertInvariant()
		return configuration
	}
	
	/**
	 * inputProjectId :: Integer 
	 */
	def inputProjectId() {
		println "which project id do you want to import?"
		print "projectId:"
		
		def projectId
		def scan = new Scanner(System.in)
		try {
			projectId = scan.nextInt()
		} catch (Exception e) {
			println "Please input number:"
			projectId = inputProjectId()
		}
		return projectId
	}
}

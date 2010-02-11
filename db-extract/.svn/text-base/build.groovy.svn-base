class build {
	
	def ant
	def classpath
	def base_dir = "./"
	def src_dir = base_dir + "src"
	def lib_dir = base_dir + "lib"
	def build_dir = base_dir + "target/classes"
	def dist_dir = base_dir + "target"
	def file_name = "tracker-etl-0.1"
	
	build () {
		ant = new AntBuilder()
		ant.taskdef(name: "groovyc", classname: "org.codehaus.groovy.ant.Groovyc", classpath: "/lib/groovy-all-1.7.0.jar")
		
		classpath = ant.path {
			fileset(dir: "${lib_dir}"){
				include(name: "*.jar")
			}
			pathelement(path: "${build_dir}")
		}
	}
	
	def clean() {
		ant.delete(dir: "${build_dir}")
		ant.delete(dir: "${dist_dir}")
	}
	
	def compile() {
		ant.mkdir(dir: "${build_dir}")
		ant.groovyc(destdir: "${build_dir}", srcdir: "${src_dir}", classpath: "${classpath}", )       
	}
	
	def jar() {
		clean()
		compile()
		ant.mkdir(dir: "${dist_dir}")
		ant.jar(destfile: "${dist_dir}/${file_name}.jar", basedir: "${build_dir}")
		{
			manifest {
				attribute( name: 'Main-Class', value: "com.ebay.tracker.Main" )
				attribute( name: 'Class-Path', value: "../lib/ojdbc14.jar ../lib/groovy-all-1.7.0.jar " +
						"../lib/commons-beanutils-1.8.2.jar ../lib/commons-collections-3.2.1.jar " +
						"../lib/commons-lang-2.4.jar ../lib/commons-logging-1.1.1.jar " +
						"../lib/ezmorph-1.0.6.jar ../lib/json-lib-2.3-jdk15.jar ../lib/log4j-1.2.15.jar" )
			}
		}
	}
	
	static main(args) {
		def b = new build()
		b.run(args)
	}
	
	def run(args) {
		if ( args.size() > 0 ) { 
			invokeMethod(args[0], null )
		} else {
			compile()
		} 
	}
}

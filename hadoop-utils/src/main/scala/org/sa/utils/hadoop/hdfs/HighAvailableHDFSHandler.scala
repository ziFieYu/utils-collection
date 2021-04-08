package org.sa.utils.hadoop.hdfs

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem

class HighAvailableHDFSHandler(nameService: String, nameNodes: Array[String], port: Int) extends FileSystemHandler {
    private val configuration = new Configuration()
    configuration.set("fs.defaultFS", s"hdfs://$nameService")
    configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem")
    configuration.set("dfs.nameservices", nameService)
    configuration.set(s"dfs.ha.namenodes.$nameService", nameNodes.mkString(","))
    nameNodes.foreach(host => configuration.set(s"dfs.namenode.rpc-address.$nameService.$host", s"$host:$port"))
    configuration.set(s"dfs.client.failover.proxy.provider.$nameService", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider")
    override protected val fileSystem: FileSystem = FileSystem.get(configuration)
}

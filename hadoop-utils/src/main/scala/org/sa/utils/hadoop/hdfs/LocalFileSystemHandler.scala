package org.sa.utils.hadoop.hdfs

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem

object LocalFileSystemHandler extends FileSystemHandler {
    override protected val fileSystem: FileSystem = FileSystem.getLocal(new Configuration())
}

package org.noear.socketd.transport.core.entity;

import org.noear.socketd.transport.core.EntityMetas;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件实体
 *
 * @author noear
 * @since 2.0
 */
public class FileEntity extends EntityDefault {
    private final RandomAccessFile fileRaf;

    public FileEntity(File file) throws IOException {
        long len = file.length();
        fileRaf = new RandomAccessFile(file, "r");
        MappedByteBuffer byteBuffer = fileRaf
                .getChannel()
                .map(FileChannel.MapMode.READ_ONLY, 0, len);

        dataSet(byteBuffer);
        metaPut(EntityMetas.META_DATA_DISPOSITION_FILENAME, file.getName());
    }

    @Override
    public void release() throws IOException {
        fileRaf.close();
    }
}

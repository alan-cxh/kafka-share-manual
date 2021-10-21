package com.alan.kafkaexample.consume.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class User implements Serializable {
    private String id;
    private String name;
    private String birthday;
    private String homeAddr;
}

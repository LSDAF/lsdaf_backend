package com.lsadf.lsadf_backend.constants;

public class JsonViews {
    public interface External {}
    public interface Internal extends External {}
    public interface Admin extends Internal {}
    public interface Default {}

    public enum JsonViewType {
        EXTERNAL, INTERNAL, ADMIN
    }
}

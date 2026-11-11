from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class GetRecordRequest(_message.Message):
    __slots__ = ("index",)
    INDEX_FIELD_NUMBER: _ClassVar[int]
    index: int
    def __init__(self, index: _Optional[int] = ...) -> None: ...

class GetRecordResponse(_message.Message):
    __slots__ = ("record",)
    RECORD_FIELD_NUMBER: _ClassVar[int]
    record: str
    def __init__(self, record: _Optional[str] = ...) -> None: ...

class AddRecordRequest(_message.Message):
    __slots__ = ("index", "record")
    INDEX_FIELD_NUMBER: _ClassVar[int]
    RECORD_FIELD_NUMBER: _ClassVar[int]
    index: int
    record: str
    def __init__(self, index: _Optional[int] = ..., record: _Optional[str] = ...) -> None: ...

class AddRecordResponse(_message.Message):
    __slots__ = ("replaced",)
    REPLACED_FIELD_NUMBER: _ClassVar[int]
    replaced: bool
    def __init__(self, replaced: _Optional[bool] = ...) -> None: ...

class GetSizeRequest(_message.Message):
    __slots__ = ()
    def __init__(self) -> None: ...

class GetSizeResponse(_message.Message):
    __slots__ = ("size",)
    SIZE_FIELD_NUMBER: _ClassVar[int]
    size: int
    def __init__(self, size: _Optional[int] = ...) -> None: ...

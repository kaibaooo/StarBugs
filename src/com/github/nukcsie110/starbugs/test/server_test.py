#!/bin/python

from pwn import *

conn = remote("127.0.0.1", 8787)

def makePacket(pkID, data):
    pkID = (pkID&0xF).to_bytes(1, byteorder="little")
    length = p32(len(data), endian='big')
    return pkID+length+data

for i in range(100):
    conn.send(makePacket(0x00, b'0123456789012345678901234567890\x00')[0:i%37])
    conn.send(makePacket(0x00, b'0123456789012345678901234567890\x00')[i%37:])
conn.send(makePacket(0x07, b'\x00'))
print(conn.recvall())

conn.close()

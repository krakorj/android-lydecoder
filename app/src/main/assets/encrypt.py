#!/usr/bin/python
# -*- coding: utf-8 -*-

import base64
import hashlib
from Crypto import Random
from Crypto.Cipher import AES

#
# For more see https://stackoverflow.com/questions/44497058/issue-with-key-and-iv-on-aes-256-cbc
#

class AESCipher(object):

    def __init__(self, password): 
        self.bs = 16
        self.key = hashlib.md5(password.encode('utf8')).digest()

    def encrypt(self, raw):
        raw = self._pad(raw)
        iv = Random.new().read(AES.block_size)
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        print "iv: " + base64.b64encode(iv) + " len:" + str(len(iv))
        print "key: " + base64.b64encode(self.key) + " len:" + str(len(self.key))
        return base64.b64encode(iv + cipher.encrypt(raw))

    def decrypt(self, enc):
        enc = base64.b64decode(enc)
        iv = enc[:AES.block_size]
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        return self._unpad(cipher.decrypt(enc[AES.block_size:])).decode('utf-8')

    def _pad(self, s):
        return s + (self.bs - len(s) % self.bs) * chr(self.bs - len(s) % self.bs)

    @staticmethod
    def _unpad(s):
        return s[:-ord(s[len(s)-1:])]

# Open file
f = open("data.csv", "r")
txt = f.read()
f.close()
print "IN:\n" + txt

# Encrypt
ac = AESCipher("124")
enc = ac.encrypt(txt)
print "OUT\n" + enc

# Save 
f = open("data.enc", "w")
f.write(enc)
f.close()


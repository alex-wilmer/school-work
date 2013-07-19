from Crypto.Cipher import AES

#Decrypt AES Cipher, implement CTR mode

key_hex = '36f18357be4dbd77f050515c73fcf9f2'
iv_hex = '69dda8455c7dd4254bf353b773304eec'
b1_hex ='0ec7702330098ce7f7520d1cbbb20fc3'
b2_hex ='88d1b0adb5054dbd7370849dbf0b88d3'
b3_hex ='93f252e764f1f5f7ad97ef79d59ce29f'
b4_hex ='5f51eeca32eabedd9afa932900000000'

stuff = [key_hex, iv_hex, b1_hex, b2_hex, b3_hex, b4_hex]
stuff_split = []
key_chars = []
iv_chars = []
b1_chars = []
b2_chars = []
b3_chars = []
b4_chars = []

m1 = []
m2 = []
m3 = []
m4 = []

#split hex
for j in range(0,6):
    stuff_split.append([stuff[j][i:i+2] for i in range(0, len(stuff[j]), 2)])

#convert hex to char
for i in stuff_split[0]:
    key_chars.append(chr(int(i, 16)))
for i in stuff_split[1]:
    iv_chars.append(chr(int(i, 16)))
for i in stuff_split[2]:
    b1_chars.append(chr(int(i, 16)))
for i in stuff_split[3]:
    b2_chars.append(chr(int(i, 16)))
for i in stuff_split[4]:
    b3_chars.append(chr(int(i, 16)))
for i in stuff_split[5]:
    b4_chars.append(chr(int(i, 16)))

#convert to strings
iv = ''.join(iv_chars)
key = ''.join(key_chars)
b1 = ''.join(b1_chars)
b2 = ''.join(b2_chars)
b3 = ''.join(b3_chars)
b4 = ''.join(b4_chars)

aes = AES.new(key)

#f[n] --> list of chars
f_of_b1 = list(aes.encrypt(iv))
iv_list = list(iv)
iv_num = []
for i in range(0,16):
    iv_num.append(ord(iv_list[i]))
	
#increment counter, repeat function
iv_num[15] += 1
for i in range(0,16):
    iv_chars[i] = chr(iv_num[i])
iv = ''.join(iv_chars)
f_of_b2 = list(aes.encrypt(iv))

#increment counter, repeat function
iv_num[15] += 1
for i in range(0,16):
    iv_chars[i] = chr(iv_num[i])
iv = ''.join(iv_chars)
f_of_b3 = list(aes.encrypt(iv))

#increment counter, repeat function
iv_num[15] += 1
for i in range(0,16):
    iv_chars[i] = chr(iv_num[i])
iv = ''.join(iv_chars)
f_of_b4 = list(aes.encrypt(iv))

#xor cipher with function pad
for i in range(0,16):
    m1.append(chr(ord(f_of_b1[i]) ^ ord(b1_chars[i])))
for i in range(0,16):
    m2.append(chr(ord(f_of_b2[i]) ^ ord(b2_chars[i])))
for i in range(0,16):
    m3.append(chr(ord(f_of_b3[i]) ^ ord(b3_chars[i])))
for i in range(0,16):
    m4.append(chr(ord(f_of_b4[i]) ^ ord(b4_chars[i])))

print ''.join(m1) + ''.join(m2) + ''.join(m3) + ''.join(m4)

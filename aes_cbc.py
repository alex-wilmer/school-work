from Crypto.Cipher import AES

#Decrypt AES Cipher, implement CBC mode

key_hex = '140b41b22a29beb4061bda66b6747e14'
iv_hex = '4ca00ff4c898d61e1edbf1800618fb28'
b1_hex ='28a226d160dad07883d04e008a7897ee'
b2_hex ='2e4b7465d5290d0c0e6c6822236e1daa'
b3_hex ='fb94ffe0c5da05d9476be028ad7c1d81'

stuff = [key_hex, iv_hex, b1_hex, b2_hex, b3_hex]
stuff_split = []
key_chars = []
iv_chars = []
b1_chars = []
b2_chars = []
b3_chars = []

m1 = []
m2 = []
m3 = []

#split hex
for j in range(0,5):
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

#convert to strings
iv = ''.join(iv_chars)
key = ''.join(key_chars)
b1 = ''.join(b1_chars)
b2 = ''.join(b2_chars)
b3 = ''.join(b3_chars)

aes = AES.new(key)
#f[n] --> list of chars
f_of_b1 = list(aes.decrypt(b1))
f_of_b2 = list(aes.decrypt(b2))
f_of_b3 = list(aes.decrypt(b3))

#f[b1] xor iv
for i in range(0,16):
  m1.append(chr(ord(f_of_b1[i]) ^ ord(iv_chars[i])))
	
#f[b2] xor b1
for i in range(0,16):
	m2.append(chr(ord(f_of_b2[i]) ^ ord(b1_chars[i])))

#f[b3] xor b2
for i in range(0,16):
	m3.append(chr(ord(f_of_b3[i]) ^ ord(b2_chars[i])))

print ''.join(m1) + ''.join(m2) + ''.join(m3)





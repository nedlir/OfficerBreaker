<h1> Officer Breaker</h1>

![useless_security_lock](readme/useless_lock.png)

Officer Breaker is a simple program that removes the password from read-only protected files that belong to the Office Open XML format family (***.pptx/.xlsx/.docx*** file types). 

Please read the [disclaimer](#disclaimer)  before using this program or its source code.

GUI artwork by [@Cioccolatodorima](https://twitter.com/cioccolato_kun).

## Requirements and Installation
If you wish to build the project yourself you need:
1. [JDK 8+](https://openjdk.org)
2. [Apache Ant ](https://ant.apache.org)
3. Go to the main repository folder and run:
```bash
ant -buildfile build.xml
```
4. Open the executable file from ```.dist/``` folder of this repo.

Alternatively, download the prebuilt executable directly from the [releases](https://github.com/nedlir/OfficerBreaker/releases) page.

## How does it work?
All pptx/xlsx/docx files are part of the Office Open XML format family (for further reading please refer to [OOXML Format Family -- ISO/IEC 29500 and ECMA 376
](https://www.loc.gov/preservation/digital/formats/fdd/fdd000395.shtml)).


For example, a standard ***.pptx*** file will have the following file tree structure:
```
myFile.pptx
.
├── docProps
│   ├── app.xml
│   ├── core.xml
│   ├── custom.xml
│   └── thumbnail.jpeg
├── ppt
│   ├── handoutMasters
│   ├── media
│   ├── media
│   ...
│   ...
│   ...
│   └── presentation.xml
├── _rels
│   ├── .rels
└── [Content_Types].xml
```
We can see this structure by opening the file using a program like [7zip](https://www.7-zip.org/) or by changing the filetype to ***.zip*** and then opening it.

Each OOXML file type contains an ***.xml*** file with settings and preferences, including read-only protection. In our example the security element will be located inside ```presentation.xml``` file which is located inside the ```ppt``` folder of ```myFile.pptx```.

Inside ```presentation.xml``` there is a specific element we will focus on called **p:modifyVerifier** which should look like this:

```python
<p:modifyVerifier cryptProviderType="rsaAES" cryptAlgorithmClass="hash" cryptAlgorithmType="typeAny" cryptAlgorithmSid="14" spinCount="100000" saltData="3R1lmtJocEj5GzEGRn3MHA==" hashData="iR0jIUtVcGsTx62z/hqcbzaReLJemv$eZyqTlpWhl0Lph+osBKEiEYmyReJHmypMy6wj+VFmDGuNZvsMA9tX9g=="/>
```
The file editing is protected by a password which was salted and hashed which makes it nearly impossible to crack within reasonable time. But instead of trying to crack the password, we can just... Remove it. :shrug:

Turns out that simply deleting the security element **p:modifyVerifier** as a whole will make ```myFile.pptx``` behave as if it never had any password at all. This kind of security measure is a bit like the photo in the title of this repository - a good lock placed on the door handle... :sweat_smile:


The program will create a copy of ```presentation.xml```, parse it and delete the security element. Once the element is deleted, the *copied* ```presentation.xml``` will be replaced with the *original* ```presentation.xml``` which will effectively remove the password from ```myFile.pptx```.

What makes this whole thing worse is the fact that we could simply remove the password created by the author, alter the file in some way and then return the original password of the author by inserting the same security element which was removed. This hurts the integrity of the whole OOXML format family.

### Future changes / possible deprecation

Future versions of OOXML file type may make drastic changes of naming convention of elements or/and structure of the folders or/and files. This might make this repo deprecated but not obsolete if the same security measures will be taken in future versions.
If a "lock with different name" (the hashing) will be placed on the "door handle" (removable ```.xml``` element), the file still could be altered/edited without a password. All that's needed is to find the new security element and simply remove it from the file.

## Screens
![app_screens](readme/app.gif)

## Disclaimer
This repository is for research purposes only, the use of this code is your responsibility.

I take no responsibility and/or liability for how you choose to use any of the source code available here. By using any of the files available in this repository, you understand that you are agreeing to use it only on files you own or received permission to use the code on. 

This repository does not promote any hacking related activity. All the information in this repository is for educational purposes only.

Once again, all files available here are for education and/or research purposes ONLY.

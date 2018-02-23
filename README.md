# Avrotool 
 
A CLI tool for comparing Avro schemas and working with the Confluent flavour of Avro.


# Why do I need this tool?

* the Confluent Schema Registry contains a registry client which can be used to check schema compatbility but of course this requires making HTTP calls, and sometimes you just want to check compatibility directly without doing any network operations
* the standard Apache Avro tools do not understand anything about schema registries, or the Confluent Avro binary format (which contains a 5 byte prefix)


# Example usages

**Check compatibility between Avro schemas**

Check the compatibility level of schema4 with each of schemas 1-3:

    $ avrotool --checkcompat --schema-files schema4.avsc schema3.avsc schema2.avsc schema1.avsc
    FULL BACKWARD FULL

Check if schema4 has at least BACKWARD compatibility with each of schemas 1-3 (exit code will be non-zero if the minimum level is not met by each):

    $ avrotool --checkcompat \
            --schema-files schema4.avsc schema3.avsc schema2.avsc schema1.avsc  \
            --level BACKWARD
    FULL BACKWARD FULL

You can get JSON output using `--format json`:

    $ avrotool --checkcompat --schema-files schema2.avsc schema1.avsc --format json
    {"schema1.json":"FULL"}


**Check an Avro schema is valid** 

    $ avrotool --validate-schema --schema-file schema.json
    VALID


**Decode a Confluent Avro binary datum and print as JSON (fetching the schema from the schema registry)**

    $ avrotool --decode --datum-file foo.cavro --schema-registry-url http://myregistry.com/
    { "name": "fred" }

**Convert a Confluent Avro binary datum to a plain Avro binary datum** 

    $ avrotool --unwrap-datum --datum-file datum.cavro > datum.avro


**Convert a plain Avro binary datum to a Confluent Avro binary datum** 

    $ avrotool --wrap-datum --datum-file datum.avro --schema-id 123 > datum.cavro


# Installation (non-docker)

Requirements:

* Java 8 or greater 
* [sbt](https://www.scala-sbt.org/download.html) (e.g. `brew install sbt`) 

Then clone this project and then from the top level run

    $ make build
    $ sudo make install PREFIX=/opt/local

To avoid sudo you could install under your home directory with `make install PREFIX="$HOME"` which will install avrotool 
to `~/bin` or `make install` which installs avrotool to `~/local/bin` (tip: in this case, add `~/local/bin` to your `PATH`).

Check it works: 

    $ avrotool --checkcompat --schema-files ./examples/schema1.json ./examples/schema2.json
    FULL


# Schema compatibility explained

Compatibility level can be one of "BACKWARD", "FORWARD", "FULL" or "NONE" (i.e. following the [confluent convention](https://docs.confluent.io/current/avro.html)).

If Schema B is *BACKWARD COMPATIBLE* with Schema A, it means B can read records written with A (a.k.a *"CAN READ"* in Avro terminology).

=> the new schema supports old writers (upgraded consumers can still process older format data)

If Schema B is *FORWARD COMPATIBLE* with Schema A, it means B can write records that can be read by A (a.k.a. *"CAN BE READ"* in Avro terminology).

=> the new schema supports old readers (upgraded producers will not break old consumers)

FULLY COMPATIBLE means both BACKWARD and FORWARD compatible.


Effects of different types of change
------------------------------------

If you drop an existing field that lacks a default value:
* this is backwards compatible - the new schema will ignore the field in old data
* this is not forwards compatible - the old schema will not be able to find or generate a value for that field

If you add a new field that lacks a default value:
* this is not backwards compatible - the new schema will not be able to find or generate a value for that field
* this is forwards compatible - the old schema will ignore the new field

If you remove or add a "default" value from/to an existing field:
* this is backwards compatible
* this is forwards compatible

If fields being added/removed have default values then those changes will be fully compatible.

Compatibility is not transitive. It's possible for A to be compatible with B, and B to be compatible with C, but for
A not to be compatible with C. For this reason, unless you have control over which schema versions are in use at a
given time, checking compatibility against all previous versions of a schema is recommended.


Example of a sequence of changes
================================

Consider the following sequence of 4 schemas where (d) means "field has a default value"

    schema1 : name (d)
    schema2 : name, number (d)
    schema3 : name, number, colour (d)
    schema4 : number (d), colour

Then the following pairwise compatibility relationships exist:

    4->3: BACKWARDS
    4->2: NONE
    4->1: FORWARDS
    
    3->2: FULL
    3->1: FORWARDS
    
    2->1: FULL


Schema authoring
================

Let's say you have a field "address" of record type (say the record is of type "example.Address") and you want the
flexibility in future to be able to remove this "address" field and still be fully compatible.

You can choose either of two options:

1. you want reading with old schema to give a null example.Address

2. you want reading with old schema to give a default example.Address

Either way, you must set `"default": null` on the "address" field (yes, no quotes).

Then for option 1, set the type of address to be unioned with "null" (yes you need the quotes) as in:
 
    "type": [ "null", { "type": "record", "name": "Address", ... } ]

For option 2, don't do a union type but instead just provide defaults for each field of example.Address.


TODO
====

* Improve validate-schema. Simply parsing is not enough. e.g. if default value has wrong type it will parse ok but
fail at runtime with something like "Exception in thread "main" org.apache.avro.AvroTypeException: Non-string default value for string: null"
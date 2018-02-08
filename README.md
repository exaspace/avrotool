# Avrotool 
 
A CLI tool for comparing Avro schemas and working with the Confluent flavour of Avro.


# Why do I need this tool?

* the Confluent Schema Registry contains a registry client which can be used to check schema compatbility but of course this requires making HTTP calls, and sometimes you just want to check compatibility directly without doing any network operations
* the standard Apache Avro tools do not understand anything about schema registries, or the Confluent Avro binary format (which contains a 5 byte prefix)


# Example usage

Check the compatibility level of schema4 with each of schemas 1-3:

    $ avrotool --checkcompat --schema-files schema4.avsc schema3.avsc schema2.avsc schema1.avsc
    FULL BACKWARD FULL

Check if schema4 has at least BACKWARD compatibility with each of schemas 1-3 (exit code will be non-zero if the minimum level is not met by each):

    $ avrotool --checkcompat --schema-files schema4.avsc schema3.avsc schema2.avsc schema1.avsc  --level BACKWARD
    FULL BACKWARD FULL

For JSON output use `--format json`:

    $ avrotool --checkcompat --schema-files schema2.avsc schema1.avsc --format json
    {"schema1.json":"FULL"}

Decode a Confluent Avro datum

    $ avrotool --decode --datum foo.avro --schema-registry-url http://myregistry.com/



# Installation (non-docker)

Install a jdk and sbt, then from the top level of the project run:

    sbt pack
    
This builds an executable you can run with `./target/pack/bin/avrotool`. The following should work:

    $ ./target/pack/bin/avrotool --checkcompat ./examples/schema1.json ./examples/schema2.json
    FULL


# Compatibility explained

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

4->3: BACKWARDS
4->2: NONE
4->1: FORWARDS

3->2: FULL
3->1: FORWARDS

2->1: FULL

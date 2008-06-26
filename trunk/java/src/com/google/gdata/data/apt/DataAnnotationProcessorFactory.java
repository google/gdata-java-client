/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.data.apt;

import com.google.gdata.data.Kind;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.util.Types;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The DataAnnotationProcessFactory class supports auto-generation of
 * metadata about GData Kind data model classes.   Generation is handled
 * by implementing the {@link AnnotationProcessor} interfaces defined for
 * the Sun Annotation Processing Tool (APT).
 * <p>
 * The processing can be extended later for any other data-model related
 * annotation usage.
 *
 * 
 */
public class DataAnnotationProcessorFactory
    implements AnnotationProcessorFactory {

  /**
   * The list of annotation types handled by this factory.
   */
  private static List<String> supportedTypes =
      Collections.unmodifiableList(
          Arrays.asList("com.google.gdata.data.*"));

  /**
   * The DataAnnotationProcessor class provides annotation processing for the
   * {@link Kind.Term} annotation.  It generates schema-to-handler
   * mapping files in META-INF/services which can be used to discover
   * the {@link Kind.Adaptor} class for a given Kind at runtime.
   */
  private static class DataAnnotationProcessor implements AnnotationProcessor {

    private AnnotationProcessorEnvironment env;

    private DataAnnotationProcessor(AnnotationProcessorEnvironment env) {

      this.env = env;
    }

    /**
     * Handles the processing and metadata generation associated with the
     * {@link Kind.Term} annotation.
     */
    private void handleKindTerms() {

      Messager msg = env.getMessager();
      Filer filer = env.getFiler();

      AnnotationTypeDeclaration kindDecl = (AnnotationTypeDeclaration)
        env.getTypeDeclaration(Kind.Term.class.getName());
      if (kindDecl == null) {
        msg.printError("Unable to find the Kind.Term annotation type");
        return;
      }

      Types typeUtils = env.getTypeUtils();
      TypeDeclaration intfDecl =
        env.getTypeDeclaration(Kind.Adaptor.class.getName());
      InterfaceType declaratorType =
        (InterfaceType)typeUtils.getDeclaredType(intfDecl);

      // Used to build a mapping from kind term values to adaptor class names.
      Map <String, List<String>> adaptorMap =
        new HashMap<String, List<String>>();

      /*
       * Phase 1: build an in-memory mapping from kind term values to
       * the list of implementing adaptor class names.
       */
      for (Declaration decl : env.getDeclarationsAnnotatedWith(kindDecl)) {

        // Annotation is only valid on clasess
        if (! (decl instanceof ClassDeclaration)) {
          msg.printError(decl.getPosition(),
              "@Kind.Term may only be used to annotate a class");
          continue;
        }

        // The target class must implement Kind.Adaptor
        ClassDeclaration classDecl = (ClassDeclaration)decl;
        ClassType classType = (ClassType)typeUtils.getDeclaredType(classDecl);
        if (!typeUtils.isAssignable(classType, declaratorType)) {
          msg.printError(classDecl.getPosition(),
              "Class annotated by @Kind.Term must implement Kind.Adaptor");
          continue;
        }

        Kind.Term kindTerm = classDecl.getAnnotation(Kind.Term.class);

        List<String> kindAdaptors = adaptorMap.get(kindTerm.value());
        if (kindAdaptors == null) {
          kindAdaptors = new ArrayList<String>();
          adaptorMap.put(kindTerm.value(), kindAdaptors);
        }
        kindAdaptors.add(classDecl.toString());
      }

      /*
       * Phase 2: write out a GData kind service mapping file for each
       * term discovered in phase 1.
       */
      for (String term : adaptorMap.keySet()) {

        String kindService = Kind.getKindServiceName(term);
        File servicePath = new File(kindService);

        PrintWriter pw = null;
        try {
          pw = filer.createTextFile(Filer.Location.CLASS_TREE, "",
              servicePath, null);
          pw.println("# GData Kind Adaptors for " + term);
          for (String adaptorClass : adaptorMap.get(term)) {
            pw.println(adaptorClass);
          }
        } catch (IOException ioe) {
          msg.printError("Unable to write kind metadata:" + servicePath);
          ioe.printStackTrace();
        } finally {
          if (pw != null) {
            pw.close();
          }
        }
        msg.printNotice("Wrote kind metadata for " + term + " to " 
            + servicePath);
      }
    }

    public void process() {
      handleKindTerms();
    }
  }

  public Collection<String> supportedOptions() {
    return Collections.emptyList();
  }

  public Collection<String> supportedAnnotationTypes() {
    return supportedTypes;
  }

  public AnnotationProcessor getProcessorFor(
      Set<AnnotationTypeDeclaration> atds,
      AnnotationProcessorEnvironment env) {
    return new DataAnnotationProcessor(env);
  }
}

// *** WARNING: DO NOT MODIFY *** This is a generated Java source code! 
// 
// Generated by LF-ET 2.1.5 (190905b), http://www.lohrfink.de/lfet
// From decision table
// "/data/github/asciidoctorj-liquibase-extension/src/main/resources/LiquibaseParseChangelogFile.lfet"
// 27.10.2019 22:22
// 
// Prolog Standard ---->
// profile LFET.Java.Prolog.Standard.Interface.ini not found
// used LF-ET 2.1.5 (190905b) build in default

package org.uniqueck.asciidoctorj.liquibase.lfet;

public interface ILiquibaseParseChangelogFile
{
 
    // Prolog Standard <----

    // Prolog Decision Table ---->
    // $$Package=org.uniqueck.asciidoctorj.liquibase.lfet
    // $$InterfaceTraceAfterRule=true
    // $$InterfaceRuleGroups = true
    // $$InterfaceName=ILiquibaseParseChangelogFile
    // $$InterfaceRulesClassname=LiquibaseParseChangelogFileRules
    // Prolog Decision Table <----
    
    /** 
     * <b>C01: Element = createTable</b>
     */
    public boolean isElementEqualCreateTable();
    
    /** 
     * <b>C02: Element = include</b>
     */
    public boolean isElementEqualInclude();
    
    /** 
     * <b>C03: Element = changeSet</b>
     */
    public boolean isElementEqualChangeSet();
    
    /** 
     * <b>C04: Element = addPrimaryKey</b>
     */
    public boolean isElementEqualAddPrimaryKey();
    
    /** 
     * <b>C05: Element = dropTable</b>
     */
    public boolean isElementEqualDropTable();
    
    /** 
     * <b>C06: Element = preConditions</b>
     */
    public boolean isElementEqualPreConditions();
    
    /** 
     * <b>C07: Element = sql</b>
     */
    public boolean isElementEqualSql();
    
    /** 
     * <b>C08: Element = createSequence</b>
     */
    public boolean isElementEqualCreateSequence();
    
    /** 
     * <b>C09: Element = createProcedure</b>
     */
    public boolean isElementEqualCreateProcedure();
    
    /** 
     * <b>C10: Element = renameColumn</b>
     */
    public boolean isElementEqualRenameColumn();
    
    /** 
     * <b>C11: Element = comment</b>
     */
    public boolean isElementEqualComment();
    
    /** 
     * <b>C12: Element = addColumn</b>
     */
    public boolean isElementEqualAddColumn();
    
    /** 
     * <b>C13: Element = dropColumn</b>
     */
    public boolean isElementEqualDropColumn();
    
    /** 
     * <b>C14: Element = rollback</b>
     */
    public boolean isElementEqualRollback();
    
    /** 
     * <b>C15: Element = dropSequence</b>
     */
    public boolean isElementEqualDropSequence();
    
    /** 
     * <b>C16: Element = addUniqueConstraint</b>
     */
    public boolean isElementEqualAddUniqueConstraint();
    
    /** 
     * <b>C17: Element = addForeignKeyConstraint</b><br>
     * <br>
     * {@code <addForeignKeyConstraint baseTableName="DEPLOYMENT_TRIGGER" baseColumnNames="DEPLOYMENTUMGEBUNG_ID" referencedTableName="DEPLOYMENT_UMGEBUNG" referencedColumnNames="ID" constraintName="DEPLOYMENT_TRIGGER_DEPLOY_FK1" />}
     */
    public boolean isElementEqualAddForeignKeyConstraint();
    
    /** 
     * <b>C18: Element = tagDatabase</b>
     */
    public boolean isElementEqualTagDatabase();
    
    /** 
     * <b>C19: tag = tillTag</b>
     */
    public boolean isTagEqualTillTag();
    
    /** 
     * <b>A01: extract Table</b>
     */
    public void doExtractTable();
    
    /** 
     * <b>A02: follow include</b>
     */
    public void doFollowInclude();
    
    /** 
     * <b>A03: parse changeSet</b>
     */
    public void doParseChangeSet();
    
    /** 
     * <b>A04: set primary key for column</b>
     */
    public void doSetPrimaryKeyForColumn();
    
    /** 
     * <b>A05: drop Table</b>
     */
    public void doDropTable();
    
    /** 
     * <b>A06: rename Column</b>
     */
    public void doRenameColumn();
    
    /** 
     * <b>A07: add Column</b>
     */
    public void doAddColumn();
    
    /** 
     * <b>A08: drop Column</b>
     */
    public void doDropColumn();
    
    /** 
     * <b>A09: addForeignKeyConstraint</b><br>
     * <br>
     * {@code <addForeignKeyConstraint baseTableName="DEPLOYMENT_TRIGGER" baseColumnNames="DEPLOYMENTUMGEBUNG_ID" referencedTableName="DEPLOYMENT_UMGEBUNG" referencedColumnNames="ID" constraintName="DEPLOYMENT_TRIGGER_DEPLOY_FK1" />}
     */
    public void doAddForeignKeyConstraint();
    
    /** 
     * <b>A10: log unsupported element</b>
     */
    public void doLogUnsupportedElement();
    
    /** 
     * <b>A11: ignore element</b>
     */
    public void doIgnoreElement();
    
    /** 
     * <b>A12: skip tag</b>
     */
    public void doSkipTag();
    
    /** 
     * <b>A13: finish parsing</b>
     */
    public void doFinishParsing();
    
    public void doTrace(java.lang.String dtName, java.lang.String version, int rules, int rule);
    
    public void doTraceAfterRule(java.lang.String dtName, java.lang.String version, int rules, int rule);

    // Epilog Standard ---->
    // profile LFET.Java.Epilog.Standard.Interface.ini not found
    // used LF-ET 2.1.5 (190905b) build in default

}
 
// Epilog Standard <----

// End of generated Java source code
// Generated by LF-ET 2.1.5 (190905b), http://www.lohrfink.de/lfet


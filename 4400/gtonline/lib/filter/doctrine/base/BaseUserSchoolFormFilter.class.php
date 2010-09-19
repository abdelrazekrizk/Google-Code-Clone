<?php

/**
 * UserSchool filter form base class.
 *
 * @package    gtonline
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormFilterGeneratedTemplate.php 29570 2010-05-21 14:49:47Z Kris.Wallsmith $
 */
abstract class BaseUserSchoolFormFilter extends BaseFormFilterDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'userId'         => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('User'), 'add_empty' => true)),
      'schoolId'       => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('School'), 'add_empty' => true)),
      'graduationYear' => new sfWidgetFormFilterInput(),
    ));

    $this->setValidators(array(
      'userId'         => new sfValidatorDoctrineChoice(array('required' => false, 'model' => $this->getRelatedModelName('User'), 'column' => 'id')),
      'schoolId'       => new sfValidatorDoctrineChoice(array('required' => false, 'model' => $this->getRelatedModelName('School'), 'column' => 'id')),
      'graduationYear' => new sfValidatorSchemaFilter('text', new sfValidatorInteger(array('required' => false))),
    ));

    $this->widgetSchema->setNameFormat('user_school_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'UserSchool';
  }

  public function getFields()
  {
    return array(
      'id'             => 'Number',
      'userId'         => 'ForeignKey',
      'schoolId'       => 'ForeignKey',
      'graduationYear' => 'Number',
    );
  }
}
